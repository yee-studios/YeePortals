# YeePortals 🌀

A custom portal plugin designed exclusively for the **RevoltMC** network.

---

## 🚀 Installation

1. Download the `.jar` file from the [Releases](https://github.com/yee-studios/YeePortals/releases) section.
2. Upload the plugin to the `/plugins` folder of your server.
3. Ensure you have a permission manager installed, such as **[LuckPerms](https://luckperms.net)**.
4. Restart the server to generate the initial configuration files.

## 🛰️ Main Commands

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/portal region` | Enters area selection mode for a portal creation. Left and right click to set first and second point. | `portal.admin` |
| `/portal create <name>` | Creates a new portal at the current area. | `portal.admin` |
| `/portal connect <origin> <destination>` | Connect the portals. To interconnect, execute the command again with the parameters inverted. | `portal.admin` |
| `/portal disconnect <portal>` | Removes the destination to a portal. | `portal.admin` |
| `/portal list` | Shows the list of the portals. | `portal.admin` |
| `/portal reload` | Reloads the plugin configuration (to be implemented). | `portal.admin` |

---

## 📜 License
This project is licensed under the **GNU Affero General Public License v3.0 (AGPL-3.0)**.
