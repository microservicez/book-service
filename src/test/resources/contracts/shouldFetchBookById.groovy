package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method GET()
		url "/api/v1/books/1"
		headers {
			accept(applicationJson())
		}
	}
	response {
		status 200
		body([
			"id": "1",
			"isbn": "ISBN_RANDOM-1",
			"name": "Clean Code",
			"authorName": "Uncle Bob",
			"category": "mystery",
			"description":"",
			"price": 100
		])
		headers {
			contentType(applicationJson())
		}
	}
}