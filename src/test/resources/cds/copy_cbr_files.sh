#!/bin/bash

# Destination directory for .cbr files
DEST_DIR="cbr_files"

# Create the destination directory if it doesn't exist
mkdir -p "$DEST_DIR"

# Find all .cbr files recursively in volum_* directories and copy them to cbr_files
find volum_* -type f -name "*.cbr" -exec cp {} "$DEST_DIR" \;

echo "All .cbr files have been copied to $DEST_DIR."