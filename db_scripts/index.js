const mongodb = require('mongodb')
let mongo_client = mongodb.MongoClient
let url = "mongodb://127.0.0.1:27017/"
let dbname = "swd"

async function create_db() {
	console.log(url + dbname)
	let db = await mongo_client.connect(url + dbname)
	await db.close()
}

async function create_collection(collection) {
	console.log(url + dbname + ", collection: " + collection)
	let db = await mongo_client.connect(url)
	let dbo = db.db(dbname)
	let res = await dbo.createCollection(collection)
	await db.close()
}

async function create_index(collection, index, options) {
	console.log(url + dbname + ", collection: " + collection + ", create index: " + index + "::" + JSON.stringify(options))
	let db = await mongo_client.connect(url)
	let dbo = db.db(dbname)
	let idx = dbo.createIndex(collection, index, options)
	await db.close()
}

async function generate() {
	await create_db()
	await create_collection("account")
	await create_index("account", "username", {unique: true})
	await create_collection("post")
	await create_collection("comment")
}

generate()