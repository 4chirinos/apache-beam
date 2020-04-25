## Apache Beam Example

Examples of two batches processing jobs or pipelines implemented using Apache Beam:

* **ReadFilesAndInsertIntoDatabase:** it reads some .csv files from a bucket on Google Cloud Storage, parses each
line to a Java object and then proceeds to insert it on database.

* **ReadDatabaseAndSaveRecordsIntoFiles:** it reads records inserted by ReadFilesAndInsertIntoDatabase pipeline on database,
parses each record to String and then saves it in a file.

#### Prerequisites:

* Java 8 or higher
* Maven

#### Table of contents:

**1.** [Very brief introduction about what is Google Dataflow](/docs/dataflowIntroduction.md)

**2.** [Creating a Google Cloud Project](/docs/creatingGoogleCloudProject.md)

**3.** [Configuring database (MySQL)](/docs/creatingDatabase.md)

**4.** [Configuring bucket](/docs/creatingBucket.md)

**5.** [Generating SSH key](/docs/generatingSSHKey.md)

**6.** [Running beam pipelines](/docs/runningPipelines.md)

#### Configuring Google Cloud CLI:

* **Authentication:** gcloud auth application-default login

* **Default region:** gcloud config set {compute/region} REGION

* **Default zone:** gcloud config set {compute/zone} ZONE

**Recommended region:** us-central1

**Recommended zone:** us-central1-a