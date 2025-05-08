#!/bin/bash

echo "Installing Clojure on Linux..."

# Define installation directory in user's home
CLOJURE_INSTALL_DIR="$HOME/.clojure"
mkdir -p "$CLOJURE_INSTALL_DIR/bin"
mkdir -p "$CLOJURE_INSTALL_DIR/lib"

# Download and extract Clojure
curl -L -o clojure-tools.tar.gz https://github.com/clojure/brew-install/releases/latest/download/clojure-tools.tar.gz
tar -xzf clojure-tools.tar.gz

# Move files to installation directory
cp -R clojure-tools/* "$CLOJURE_INSTALL_DIR"

# Create wrapper scripts
cat > "$CLOJURE_INSTALL_DIR/bin/clojure" << 'EOF'
#!/bin/bash
CLOJURE_INSTALL_DIR="$HOME/.clojure"
export CLOJURE_LIBS="$CLOJURE_INSTALL_DIR/lib"
exec "$CLOJURE_INSTALL_DIR/libexec/clojure-tools/clojure" "$@"
EOF

cat > "$CLOJURE_INSTALL_DIR/bin/clj" << 'EOF'
#!/bin/bash
CLOJURE_INSTALL_DIR="$HOME/.clojure"
export CLOJURE_LIBS="$CLOJURE_INSTALL_DIR/lib"
exec "$CLOJURE_INSTALL_DIR/libexec/clojure-tools/clj" "$@"
EOF

# Make scripts executable
chmod +x "$CLOJURE_INSTALL_DIR/bin/clojure"
chmod +x "$CLOJURE_INSTALL_DIR/bin/clj"

# Add to PATH if not already there
if [[ ":$PATH:" != *":$CLOJURE_INSTALL_DIR/bin:"* ]]; then
    echo 'export PATH="$HOME/.clojure/bin:$PATH"' >> "$HOME/.bashrc"
    export PATH="$CLOJURE_INSTALL_DIR/bin:$PATH"
fi

# Clean up
rm -rf clojure-tools*

echo "Clojure installation complete!"
echo "Please run 'source ~/.bashrc' or start a new terminal to use clojure."