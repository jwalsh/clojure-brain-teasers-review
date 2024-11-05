CYAN = \033[36m
RESET = \033[0m

.PHONY: all clean test repl lint

default: help	## Default target shows help
all: clean test	## Clean and run all tests
clean:	## Remove build artifacts and caches
	rm -rf target
	rm -rf .cpcache
	rm -rf .clj-kondo/.cache
	rm -rf .lsp/.cache

test:	## Run tests using Cognitect test runner 
	clojure -M:test

repl:	## Start a Clojure REPL
	clojure -M:repl

lint:	## Run clj-kondo linter
	clj-kondo --lint src test

fmt:	## Run cljfmt code formatter
	cljfmt fix

init:	## Create initial directory structure for project
	mkdir -p src/basics
	mkdir -p src/collections  
	mkdir -p src/evaluation
	mkdir -p src/runtime
	mkdir -p test/basics
	mkdir -p test/collections
	mkdir -p test/evaluation
	mkdir -p test/runtime

deps:	## Install project dependencies
	clj -P

check: lint test	## Run all checks (lint + test)

help:	## Display this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk -F ':.*?## ' '/^[a-zA-Z_-]+:.*?## / {printf "  $(CYAN)%-12s$(RESET) %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.PHONY: init deps check fmt help
