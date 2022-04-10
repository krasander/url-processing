# URL Processing Application

This is a sample application. It processes a given URL and counts the number of paragraphs (p elements) on the webpage.
It achieves this via two endpoints that are exposed:
 * _http://localhost:8080/api/url/process_
 * _http://localhost:8080/api/url/result/{uuid}_

The _process_ endpoint accepts a _url_ param which is put in a queue and processed when resources are available.
The response is a new url which can be used to fetch the result once processing is finished.

The _result_ endpoint accepts a path variable _uuid_, which was given by the previous endpoint and retrieves the processing result.
If no such uuid exists, then 404 Not Found is returned.
If processing request was received, but not yet finised, 202 Accepted is returned.
If processing is finished, then 200 OK is returned along with the results.

### Example

POST _http://0.0.0.0:8080/api/url/process?url=http://www.columbia.edu/~fdc/sample.html_

Response: 
```json
{ "url": "/api/url/result/6ecfb52c-b96f-41c9-b3d7-de8eab7fe811" }
```
GET _http://0.0.0.0:8080/api/url/result/6ecfb52c-b96f-41c9-b3d7-de8eab7fe811_

Response:
```json
{
"url": "http://www.columbia.edu/~fdc/sample.html",
"nrOfParagraphs": 88,
"error": null,
"createdAt": "2022-04-10T18:29:53.194554",
"startedAt": "2022-04-10T18:29:53.219633",
"finishedAt": "2022-04-10T18:29:53.567753"
}
```

## Running the application

Run `gradle bootRun` to run the application.
A folder `db` is created which contains H2 database files. The data is persisted through application restarts.

## Running tests

Run `gradle test` to run controller & service tests.
Run `gradle intTest` to run repository tests.