// boss_account_v1
db.boss_account_v1.createIndex({
    "socialInfo.socialId": 1,
    "socialInfo.socialType": 1
}, {
    unique: true
});


// boss_registration_v1
db.boss_registration_v1.createIndex({
    "boss.socialInfo.socialId": 1,
    "boss.socialInfo.socialType": 1,
    "status": 1,
});

db.boss_registration_v1.createIndex({
    "status": 1,
});

db.boss_registration_v1.createIndex({
    "id": 1,
    "status": 1,
});


// boss_store_feedback_v1
db.boss_store_feedback_v1.createIndex({
    "bossStoreId": 1,
    "userId": 1,
    "feedbackType": 1,
    "date": 1
}, {
    unique: true
});

db.boss_store_feedback_v1.createIndex({
    "bossStoreId": 1,
    "date": 1
});


// boss_store_open_v1
db.boss_store_open_v1.createIndex({
    "expiredAt": 1
}, {
    expireAfterSeconds: 1800
})

db.boss_store_open_v1.createIndex({
    "bossStoreId": 1
})


// boss_store_v1
db.boss_store_v1.createIndex({
    "bossId": 1
}, {
    unique: true
})

db.boss_store_v1.createIndex({
    "location": "2dsphere"
});


// device_v1
db.device_v1.createIndex({
    "accountId": 1,
    "accountType": 1
}, {
    unique: true
});

db.device_v1.createIndex({
    "deviceInfo.pushToken": 1
}, {
    unique: true
});
