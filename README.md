# Housing

This service is used on the informational sites to provide housing data and services.

This application provides endpoints for:

* Rent pressure Zone information for a postcode and date.
* Support for generating Model Tenancy PDF's

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


# Endpoints
GET /rpz?postcode=<valid scottish postocde>&date=<date in format 2016-02-02>

Returns the rent pressure zone information for a given postcode.  Example results:
{
    "validPostcode": true,
    "scottishPostcode": true,
    "inRentPressureZone": true,
    "rentPressureZoneTitle": "Stockbridge, Edinburgh"
    "maxIncrease": 1.5
}

GET /modeltenancy/template
Returns a template model tenancy JSON document.  This will include default values for conditional terms.

POST /modeltenancy
Ruturns a filled out model tenancy PDF for the model tenancy documents retunred to it, or a list of validations
errors of the tenancy is not valid.
