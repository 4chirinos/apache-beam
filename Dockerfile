FROM openjdk:8

RUN apt update && \
    apt upgrade && \
    apt-cache search maven && \
    apt install -y maven && \
    apt-get install -y sudo && \
    echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && \
    apt-get install -y apt-transport-https ca-certificates gnupg && \
    curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add - && \
    apt-get update && apt-get install -y google-cloud-sdk

COPY . /usr/apache-beam
WORKDIR /usr/apache-beam
RUN mvn package

RUN export GOOGLE_APPLICATION_CREDENTIALS=/usr/apache-beam/gcp-key.json

CMD java -jar target/gcp-dataflow-example-bundled-0.1.jar --project=$project --bucket=$bucket --dbHost=$dbHost \
 --dbPort=$dbPort --dbName=$dbName --dbUsername=$dbUsername --dbPassword=$dbPassword --region=$region --runner=$runner \
 --subscriber=$subscriber --fixedWindowDuration=$fixedWindowDuration