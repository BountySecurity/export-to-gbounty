# ![GBounty Profiles Designer Logo](/static/logo.png)

# GBounty Profiles Designer

[![GitHub release](https://img.shields.io/github/release/bountysecurity/export-to-gbounty.svg)](https://github.com/bountysecurity/export-to-gbounty/releases)
[![GitHub last commit](https://img.shields.io/github/last-commit/bountysecurity/export-to-gbounty.svg)](https://github.com/bountysecurity/export-to-gbounty/commits/main)
[![GitHub issues](https://img.shields.io/github/issues/bountysecurity/export-to-gbounty.svg)](https://github.com/bountysecurity/export-to-gbounty/issues)
[![GitHub forks](https://img.shields.io/github/forks/bountysecurity/export-to-gbounty.svg)](https://github.com/bountysecurity/export-to-gbounty/network)
[![GitHub stars](https://img.shields.io/github/stars/bountysecurity/export-to-gbounty.svg)](https://github.com/bountysecurity/export-to-gbounty/stargazers)

# Export to GBounty

**Export to GBounty** is a Burp Suite extension developed using the Montoya API. It allows users to export selected HTTP requests from various Burp Suite tools, including the Site Map Tree, Repeater, and Message Editor, into a compressed ZIP file. This ZIP file can be directly used with the GBounty scanner using the command `gbounty -rf requests.zip`, enabling streamlined vulnerability scanning and management.

## Features

- **Effortless Export:** Easily export selected HTTP requests from multiple sources within Burp Suite.
- **Compressed Format:** Saves requests in a ZIP archive, optimizing storage and transfer.
- **Unique File Naming:** Each request is saved as a uniquely named text file within the ZIP to prevent conflicts.
- **Wide Compatibility:** Supports exporting from Site Map Tree, Repeater, Message Editor, and other compatible tools.
- **User-Friendly Interface:** Adds a context menu option "Export to GBounty" for a seamless user experience.
- **Robust Error Handling:** Provides clear notifications regarding the export status, including overwrite confirmations and error messages.

## Installation

### Prerequisites

- **Java Development Kit (JDK):** Ensure you have JDK 8 or higher installed.
- **Burp Suite:** The extension is compatible with Burp Suite Professional and Burp Suite Community.

### Usage
Select Requests to Export:

Within Burp Suite, select the HTTP requests you wish to export from the Site Map Tree, Repeater, Message Editor, or other supported tools.

### Export Requests:

Right-click on the selected requests.
Choose the Export to GBounty option from the context menu.

### Save the ZIP File:

A file chooser dialog will appear. Select the desired location and name for the ZIP file (default is requests.zip).
If the selected file already exists, you will be prompted to confirm whether to overwrite it.
Upon confirmation, the selected requests will be exported and saved in the specified ZIP file.

### Run GBounty Scanner:

Use the exported ZIP file with the GBounty scanner by executing the following command in your terminal:

```
gbounty -rf requests.zip
```

This command instructs GBounty to read the requests from the requests.zip file and perform vulnerability scanning based on the provided data.
