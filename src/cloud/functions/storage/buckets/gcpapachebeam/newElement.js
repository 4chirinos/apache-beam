const {PubSub} = require('@google-cloud/pubsub');
const pubSub = new PubSub();
const topicName = 'test-topic-1';
const monitoringFolder = 'monitoring/';

exports.handler = (event, context) => {
    console.log(event);
    if (isMonitoringFolder(event.name)) {
        publishEvent(event);
    }
};

const publishEvent = (event) => {
    const payload = {
        bucket: event.bucket,
        filePath: event.name,
        timeCreated: event.timeCreated
    };
    const data = Buffer.from(JSON.stringify(payload));
    pubSub
        .topic(topicName)
        .publish(data)
        .then(id => console.log(`${payload.filePath} was added to pubSub with id: ${id}`))
        .catch(err => console.log(err));
};

const isMonitoringFolder = filePath => filePath.search(monitoringFolder) != -1