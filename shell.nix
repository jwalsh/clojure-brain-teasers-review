{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    clojure
    gnumake
    git
    ghostscript
    imagemagick
    qrencode
    gh
  ];
}
