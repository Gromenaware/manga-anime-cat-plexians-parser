#!/bin/bash

# Loop through each volum_* directory
for VOLUM_DIR in volum_*; do
    # Check if it's a directory
    if [ -d "$VOLUM_DIR" ]; then
        # Name of the output cbr file
        CBR_NAME="${VOLUM_DIR}.cbr"

        # Find all image files in the volum (recursively), add to zip archive
        find "$VOLUM_DIR" -type f \( -iname "*.jpg" -o -iname "*.jpeg" -o -iname "*.png" \) \
            | zip -@ "${VOLUM_DIR}.zip"

        # Rename the zip file to .cbr
        mv "${VOLUM_DIR}.zip" "$CBR_NAME"

        echo "Created $CBR_NAME"
    fi
done