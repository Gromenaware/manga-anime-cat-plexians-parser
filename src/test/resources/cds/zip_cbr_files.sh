#!/bin/bash

# Loop over a range of episode numbers
for i in {1..27}; do
    # Format the episode number with leading zeros if needed
    episode_number=$(printf $i)
    echo $episode_number
    zip capitol_${episode_number}.cbr volume_1_episode_${episode_number}_*.jpg
    echo "Created capitol_${episode_number}.cbr"
done

echo "All .cbr files have been created."

