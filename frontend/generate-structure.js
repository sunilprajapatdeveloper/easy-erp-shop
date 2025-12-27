const fs = require("fs");
const path = require("path");

const baseDir = path.resolve(__dirname, "src"); // adjust if your paths differ

const targets = ["pages", "components"]; // folders to include
const ignoredDirs = new Set(["node_modules", ".git", "dist", ".vscode"]);

function generateTree(dir, prefix = "") {
  let tree = "";

  if (!fs.existsSync(dir)) return tree;

  const items = fs.readdirSync(dir).filter(item => !ignoredDirs.has(item));
  const lastIndex = items.length - 1;

  items.forEach((item, index) => {
    const fullPath = path.join(dir, item);
    const isDir = fs.statSync(fullPath).isDirectory();
    const connector = index === lastIndex ? "└── " : "├── ";

    tree += `${prefix}${connector}${item}\n`;

    if (isDir) {
      const newPrefix = prefix + (index === lastIndex ? "    " : "│   ");
      tree += generateTree(fullPath, newPrefix);
    }
  });

  return tree;
}

let fullStructure = "";

targets.forEach(target => {
  const targetPath = path.join(baseDir, target);
  fullStructure += `📁 ${target.toUpperCase()}\n`;
  fullStructure += generateTree(targetPath, "") + "\n";
});

// Save to file
fs.writeFileSync("pages-components-structure.txt", fullStructure);

console.log("✅ Pages and Components structure saved to pages-components-structure.txt");
