CYAN = \033[36m
RESET = \033[0m
CLJ = clj -M

.PHONY: build clean test repl lint fmt init deps check help cover qr

# --- Default ---
default: help ## Default target shows help

# --- Build ---
build: test ## Build the project (alias for test)

# --- Clean ---
clean: ## Remove build artifacts and caches
	rm -rf target
	rm -rf .cpcache
	rm -rf .clj-kondo/.cache
	rm -rf .lsp/.cache
	rm -f book-cover.png
	rm -f qr-code.png

# --- Test ---
test: ## Run tests using Cognitect test runner
	$(CLJ):test

# --- REPL ---
repl: ## Start a Clojure REPL
	$(CLJ):repl

# --- Lint ---
lint: ## Run clj-kondo linter
	$(CLJ):dev:clj-kondo --lint src test

# --- Format ---
fmt: ## Run cljfmt code formatter
	$(CLJ):dev:cljfmt fix

# --- Initialize ---
init: ## Create initial directory structure for project
	mkdir -p src/basics
	mkdir -p src/collections
	mkdir -p src/evaluation
	mkdir -p src/runtime
	mkdir -p test/basics
	mkdir -p test/collections
	mkdir -p test/evaluation
	mkdir -p test/runtime

# --- Dependencies ---
deps: ## Install project dependencies
	clj -P

# --- Check ---
check: lint test ## Run all checks (lint + test)

# --- Help ---
help: ## Display this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk -F ':.*?## ' '/^[a-zA-Z_-]+:.*?## / {printf "  $(CYAN)%-12s$(RESET) %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# --- Cover ---
cover: book-cover.png ## Generate the book cover (alias)

book-cover.png: clojure-brain-teasers_B2.0.pdf ## Show the book cover
	@TMP=$(mktemp); \
	gs -dFirstPage=1 -dLastPage=1 -sDEVICE=pngalpha -o $$TMP.png -r300 $< -q; \
	convert $$TMP.png -resize 480x $@; \
	rm $$TMP.png

# --- QR ---
qr: qr-code.png ## Generate terminal and PNG QR codes for the repository URL (alias)

qr-code.png: ## Generate a PNG QR code for the repository URL
	@gh browse -n | tee >(qrencode -t PNG -o $@) | qrencode -t UTF8
