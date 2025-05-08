#!/bin/bash

# Colors
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Create directories
mkdir -p downloads
mkdir -p extracted

# Download resources
echo -e "${YELLOW}Downloading Clojure Brain Teasers resources...${NC}"

# Download code from pragprog
echo -e "${BLUE}Downloading code from pragprog.com...${NC}"
curl -L -o downloads/mmclobrain-code.zip https://media.pragprog.com/titles/mmclobrain/code/mmclobrain-code.zip

# Check if download was successful
if [ $? -eq 0 ]; then
  echo -e "${GREEN}Successfully downloaded code archive${NC}"
  
  # Extract the code
  echo -e "${BLUE}Extracting code...${NC}"
  unzip -q -o downloads/mmclobrain-code.zip -d extracted
  
  if [ $? -eq 0 ]; then
    echo -e "${GREEN}Successfully extracted code to 'extracted/code/'${NC}"
    echo -e "${BLUE}Files downloaded and extracted from P1.0 release (2025/02/27)${NC}"
  else
    echo -e "Error extracting code archive"
    exit 1
  fi
else
  echo -e "Error downloading code archive"
  exit 1
fi

echo -e "\n${GREEN}Download completed successfully!${NC}"
echo "See doc/RELEASES.md for release information and version history."