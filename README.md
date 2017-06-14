# Housing

This service is used on the informational sites to provide housing data and services.

This application provides endpoints for:

* Rent pressure Zone information for a postcode and date.

# Configuration

* `port`
  * The port number to listen on for HTTP requests.
  * Type: integer (0-65535)
  * Default: `8096`

# Monitoring

The healthcheck endpoint is `GET /health`. The endpoint returns a JSON response
with the properties listed below. The status code is `200` if the service is
healthy, and `503` otherwise.

* `ok`
  * Indicates whether the gep service is contactable.
  * Type: boolean

# Integration tests

The tests in the `scot.mygov.housing.it` package are integration tests that
depend on a running instance of geo-search. These tests can be run using
`mvn verify`.

This starts up an Elasticsearch instance as part of the maven build, and stops it after running the integration tests.