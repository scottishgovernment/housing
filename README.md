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

* `geosearch`
  * Base URI of Geosearch service
  * Type: URI
  * Default: `http://localhost:9092/`

* `aspose_license`
  * path of aspose license file
  * Type: File
  * Default: (none)

# Monitoring

The healthcheck endpoint is `GET /health`. The endpoint returns a JSON response
with the properties listed below. The status code is `200` if the service is
healthy, and `503` otherwise.

* `geosearch`
  * Indicates whether the Geosearch service is available and reporting healthy.
  * Type: boolean
* `license`
  * Indicates whether the Aspose Words license is configured and valid.
  * Type: boolean
* `message`
  * If present, a status message to be shown if the service is healthy.
  * Type: string
* `warnings`
  * If present, indicates issues that may affect service availability.
  * Type: array of strings
* `errors`
  * If present, indicates reasons that the service is unhealthy.
  * Type: array of strings
* `data`
  * Indicates .
  * Type: object
  * Properties:
    * `licenseExpires`
      * Indicates the date the Aspose Words license expires.
      * Type: string (yyyy-mm-dd)
    * `daysUntilExpiry`
      * Indicates the number of days until the Aspose Words license expires.
      * Type: number

The `/health` endpoint supports the following optional parameters:

* `licenseDays`
  * Return a warning if the Aspose Words will expire within this number of days.
  * Type: integer


# Endpoints

`GET /rpz?postcode=<postcode>&date=<date>`

The postcode parameter should be a valid Scottish postcode. The date should be
in the format yyyy-mm-dd.

Returns the rent pressure zone information for a given postcode.  Example results:
{
    "validPostcode": true,
    "scottishPostcode": true,
    "inRentPressureZone": true,
    "rentPressureZoneTitle": "Stockbridge, Edinburgh"
    "maxIncrease": 1.5
}

`GET /modeltenancy/template`

Returns a template model tenancy JSON document.  This will include default values for conditional terms.

`POST /modeltenancy`

Returns a filled out model tenancy PDF for the model tenancy documents returned
to it, or a list of validations errors of the tenancy is not valid.
