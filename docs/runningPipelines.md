#### Running pipelines

* **Executing pipelines on Google Cloud Platform:**

```sh
mvn compile exec:java \
    -Dexec.mainClass=org.apache.beam.SamplePipeline \
    -Dexec.args="--projectId=your-project-id \
    --bucket=your-bucket \
    --region=your-region \
    --dbHost=your-db-host-address \
    --dbPort=your-db-port \
    --dbName=your-db-name \
    --dbDriver=your-db-driver \
    --dbUsername=your-db-user \
    --dbPassword=your-db-password \
    --runner=DataflowRunner"
```

* **Executing pipelines locally:** In case you want to run the pipelines locally, set **--runner** to **DirectRunner**.

* **Executing pipelines in Docker container:**

**1.** Clone the project and cd the root folder

```sh
    git clone https://github.com/4chirinos/GCP-Dataflow.git
    cd GCP-Dataflow
```

**2.** Set on **env_file** the arguments to pass in to the container (these are the parameters used to run the maven command)

**3.** Open terminal and build the docker image:

```sh
    docker build -t data-flow .
```

**4.** Run the docker container:

```sh
    docker run -it --rm --env-file=env_file --name my-running-app data-flow
```