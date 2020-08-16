package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method PUT()
		url "/api/v1/books"
		headers {
			accept(applicationJson())
			contentType("application/json")
		}
		body([
			"id": 2,
			"isbn": "ISBN_RANDOM-123",
			"name": "Effective Java",
			"authorName": "Jsohua Bloch",
			"category": "technical",
			"description":"",
			"price": 400
		])
	}
	response {
		status 200
		body([
			"id": 2,
			"isbn": "ISBN_RANDOM-123",
			"name": "Effective Java",
			"authorName": "Jsohua Bloch",
			"category": "technical",
			"description":"",
			"price": 400
		])
		headers {
			contentType("application/json")
		}
	}
}