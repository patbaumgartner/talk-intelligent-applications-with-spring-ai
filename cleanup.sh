#!/usr/bin/env bash
set -x pipefail

# General Cleanup
(
    find . -name "*:Zone.Identifier" -exec rm -rf {} \;
)

# Cleanup workshop archives
(
    rm -rf *.zip
)

# General node_modules
(
    rm -rf node_modules
    rm -rf package-lock.json
)

# Cleanup generated slides
(
    rm -rf build
)

# Cleanup demos
(
    cd demos
    find . -name "*:Zone.Identifier" -exec rm -rf {} \;
    find . -name ".DS_Store" -exec rm -rf {} \;
    find . -name ".localized" -exec rm -rf {} \;
    find . -name "desktop.ini" -exec rm -rf {} \;

    # Cleanup eclipse specifc folders
    find . -name "bin" -exec rm -rf {} \;
    find . -name ".settings" -exec rm -rf {} \;
    find . -name ".classpath" -exec rm -rf {} \;
    find . -name ".project" -exec rm -rf {} \;
    find . -name ".springbeans" -exec rm -rf {} \;
    find . -name ".factorypath" -exec rm -rf {} \;

    #Cleanup ollama specific folders
    find . -name "olama" -exec rm -rf {} \;

    #Cleanup maven specific folders
    find . -name "target" -exec rm -rf {} \;

    #Cleanup gradle specific folders
    find . -name "build" -exec rm -rf {} \;
    find . -name "out" -exec rm -rf {} \;
    find . -name ".gradle" -exec rm -rf {} \;
    
    #Cleanup intellij specific folders
    find . -name ".idea" -exec rm -rf {} \;
    find . -name ".jpb" -exec rm -rf {} \;
    find . -name "*.iml" -exec rm -rf {} \;

    #Cleanup git specific folders
    #find . -name ".git" -exec rm -rf {} \;
    #find . -name ".gitignore" -exec rm -rf {} \;
)