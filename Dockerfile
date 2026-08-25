FROM maven:3.9.16-eclipse-temurin-21-alpine

RUN apk add --no-cache chromium chromium-chromedriver

WORKDIR /workspace

# MODULE selects execution scope: "all" (default), a single module name
# (e.g. "selenium-basic"), or a comma-separated subset (Maven's -pl accepts
# a list directly, e.g. "selenium-basic,selenide").
ENV MODULE=all

ENTRYPOINT ["sh", "-c", "if [ \"$MODULE\" = \"all\" ]; then mvn test; else mvn -pl \"$MODULE\" -am test; fi"]
