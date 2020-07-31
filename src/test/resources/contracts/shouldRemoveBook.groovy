package contracts

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method DELETE()
		url "/api/v1/books/1"
	}
	response {
		status 204
	}
}