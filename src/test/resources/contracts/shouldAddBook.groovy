package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method POST()
		url "/api/v1/books/"
		headers {
			accept(applicationJson())
			contentType(applicationJson())
		}
		body([
			"isbn": "ISBN_RANDOM-123",
			"name": "Effective Java",
			"authorName": "Jsohua Bloch",
			"category": "technical",
			"description":"",
			"price": 200
		])
	}
	response {
		status 201
		body([
			"isbn": "ISBN_RANDOM-123",
			"name": "Effective Java",
			"authorName": "Jsohua Bloch",
			"category": "technical",
			"description":"",
			"price": 200
		])
		headers {
			contentType(applicationJson())
		}
	}
}