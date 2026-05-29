#!/bin/bash

# Check if API key is set
if [ -z "$DASHSCOPE_API_KEY" ]; then
    echo "Error: DASHSCOPE_API_KEY environment variable is not set"
    echo "Please set it with: export DASHSCOPE_API_KEY=your_api_key"
    exit 1
fi

echo "Starting ADD 3.0 Multi-Agent System..."
# -s .mvn/settings.xml works around the global http Maven mirror that Maven 3.9+ blocks.
mvn -s .mvn/settings.xml clean spring-boot:run
