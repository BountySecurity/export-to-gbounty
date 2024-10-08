import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.sitemap.SiteMap;
import static burp.api.montoya.sitemap.SiteMapFilter.prefixFilter;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.InvocationType;
import burp.api.montoya.ui.menu.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportToGBountyExtension implements BurpExtension {

    private MontoyaApi api;
    private SiteMap siteMap;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.siteMap = api.siteMap();
        api.extension().setName("Export to GBounty");
        api.userInterface().registerContextMenuItemsProvider(new GBountyContextMenuProvider());
    }

    private class GBountyContextMenuProvider implements ContextMenuItemsProvider {

        @Override
        public List<Component> provideMenuItems(ContextMenuEvent contextMenuEvent) {
            JMenuItem exportItem = new JMenuItem("Export to GBounty");
            exportItem.addActionListener(e -> exportRequests(contextMenuEvent));
            return List.of(exportItem);
        }
    }

    private void exportRequests(ContextMenuEvent contextMenuEvent) {
        List<HttpRequestResponse> requests = new ArrayList<>();

        if (contextMenuEvent.isFrom(InvocationType.SITE_MAP_TREE)) {
            for (HttpRequestResponse baseRequestResponse : contextMenuEvent.selectedRequestResponses()) {
                String protocol = baseRequestResponse.request().httpService().secure() ? "https" : "http";
                String host = baseRequestResponse.request().httpService().host();
                List<HttpRequestResponse> httpRequestResponses = siteMap.requestResponses(prefixFilter(protocol + "://" + host));
                for (HttpRequestResponse entry : httpRequestResponses) {
                    requests.add(entry);
                }
            }
        } else {
            // Otherwise, just use the selected requests
            requests.addAll(contextMenuEvent.selectedRequestResponses());
        }

        if (requests.isEmpty()) {
            api.logging().logToOutput("No requests selected to export.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save ZIP File");
        fileChooser.setSelectedFile(new File("requests.zip"));
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File zipFile = fileChooser.getSelectedFile();
            if (zipFile.exists()) {
                int overwriteConfirm = JOptionPane.showConfirmDialog(null, "The file already exists. Do you want to overwrite it?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (overwriteConfirm != JOptionPane.YES_OPTION) {
                    api.logging().logToOutput("Export operation cancelled by user.");
                    return;
                }
            }
            try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (HttpRequestResponse requestResponse : requests) {
                    HttpService httpService = requestResponse.request().httpService();
                    String host = httpService.host();
                    String protocol = httpService.secure() ? "https" : "http";
                    String requestString = requestResponse.request().toString();

                    // Ensure unique filename by adding a UUID
                    String filename = host + "_" + UUID.randomUUID() + ".txt";
                    ZipEntry zipEntry = new ZipEntry(filename);
                    zos.putNextEntry(zipEntry);

                    String content = protocol + "://" + host + "\n" + requestString;
                    zos.write(content.getBytes());
                    zos.closeEntry();
                }

                api.logging().logToOutput("Exported " + requests.size() + " requests to " + zipFile.getAbsolutePath());
            } catch (IOException ex) {
                api.logging().logToError("Error exporting requests: " + ex.getMessage());
            }
        } else {
            api.logging().logToOutput("Export operation cancelled by user.");
        }
    }
}
