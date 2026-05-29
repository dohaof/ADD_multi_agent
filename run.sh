#!/bin/bash

# Check if API key is set
if [ -z "$DASHSCOPE_API_KEY" ]; then
    echo "Error: DASHSCOPE_API_KEY environment variable is not set"
    echo "Please set it with: export DASHSCOPE_API_KEY=your_api_key"
    exit 1
fi

echo "Starting ADD 3.0 Multi-Agent System..."
mvn clean spring-boot:run
