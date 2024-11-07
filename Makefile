# Colors
CYAN      := \033[36m
GREEN     := \033[32m
YELLOW    := \033[33m
RESET     := \033[0m

# Project structure
SRC_DIRS  := basics collections evaluation runtime
PDF       := clojure-brain-teasers_B2.0.pdf

.PHONY: all clean test repl lint fmt init deps check help cover qr default build outdated

default: help ## Show help

all: clean deps check ## Run all main targets

build: test ## Build the project (runs tests)
	@echo "$(GREEN)Build completed successfully$(RESET)"

clean: ## Clean all build artifacts
	@echo "$(YELLOW)Cleaning project...$(RESET)"
	@rm -rf target .cpcache .clj-kondo/.cache .lsp/.cache book-cover.png qr-code.png
	@echo "$(GREEN)Clean completed$(RESET)"

test: ## Run tests
	@echo "$(YELLOW)Running tests...$(RESET)"
	@clj -M:test

repl: ## Start a REPL
	@echo "$(YELLOW)Starting REPL...$(RESET)"
	@clj -M:repl

lint: ## Run the linter
	@echo "$(YELLOW)Running linter...$(RESET)"
	@clj -M:dev clj-kondo --lint src test

fmt: ## Format the code
	@echo "$(YELLOW)Formatting code...$(RESET)"
	@clj -M:dev cljfmt fix

outdated: ## Check for outdated dependencies
	@echo "$(YELLOW)Checking for outdated dependencies...$(RESET)"
	@clj -M:outdated

init: ## Create project directory structure
	@echo "$(YELLOW)Creating project structure...$(RESET)"
	@for dir in $(SRC_DIRS); do \
		mkdir -p src/$$dir test/$$dir; \
	done
	@echo "$(GREEN)Project structure created$(RESET)"

deps: ## Install dependencies
	@echo "$(YELLOW)Installing dependencies...$(RESET)"
	@clj -P
	@echo "$(GREEN)Dependencies installed$(RESET)"

check: lint test ## Run all checks (lint + test)
	@echo "$(GREEN)All checks passed$(RESET)"

book-cover.png: $(PDF) ## Generate book cover from PDF
	@echo "$(YELLOW)Generating book cover...$(RESET)"
	@TMP=$$(mktemp) && \
	gs -dFirstPage=1 -dLastPage=1 -sDEVICE=pngalpha -o $$TMP.png -r300 $< -q && \
	convert $$TMP.png -resize 480x $@ && \
	rm $$TMP.png
	@echo "$(GREEN)Book cover generated$(RESET)"

qr-code.png: ## Generate QR code for repository
	@echo "$(YELLOW)Generating QR code...$(RESET)"
	@gh browse -n | tee >(qrencode -t PNG -o $@) | qrencode -t UTF8
	@echo "$(GREEN)QR code generated$(RESET)"

cover: book-cover.png ## Generate book cover (alias)

qr: qr-code.png ## Generate QR code (alias)

help: ## Show this help
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk -F ':.*?## ' '/^[a-zA-Z_-]+:.*?## / {printf "  $(CYAN)%-12s$(RESET) %s\n", $$1, $$2}' $(MAKEFILE_LIST)
