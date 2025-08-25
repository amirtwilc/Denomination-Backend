# Denomination-Backend

## Description

This service serves as a backend service for the Denomination App, 
and performs the calculations required by it.

This service receives an amount as an input and also an optional json that contains the results from a previous calculation.

The amount will be used to return the denominations required to produce the given amount,
while always considering the largest possible denomination first.

For example: if an amount of 245.00 is provided, then the output result will be: <br>

| denomination | quantity |
|--------------|----------|
| 200.00       | 1        |
| 20.00        | 2        |
| 5.00         | 1        |

In addition, if a json of a previous result was provided, 
the output will also include a <b>difference representation</b> between the previous calculation and the current one.

For example: if an amount of 275.00 is provided and also the result from above of 245.00, then the difference result would look like this:

| denomination | difference | reason (not included in actual output)                                         |
|--------------|------------|--------------------------------------------------------------------------------|
| 200.00       | 0          | denomination stayed the same                                                   |
| 50.00        | +1         | denomination found in new amount, but not in old                               |
| 20.00        | -1         | new amount only includes 1 of this denomination, as opposed to 2 in old amount |
| 5.00         | 0          | denomination stayed the same                                                   |

* Note: the supported denomination are included in application.yaml file

## Installation

1. Install Java 21 and Maven if not installed.
2. Run DenominationApplication to run this service on localhost:8080
3. Use the frontend Denomination App to use this service, or call this service directly via Postman

## Usage

To call this service directly with Postman, import these examples:

### Only with amount:
curl --location --request POST 'http://localhost:8080/denomination/v1/calculate/0.03'

### With optional json of previous result:
curl --location 'localhost:8080/denomination/v1/calculate/22.3' \
--header 'Content-Type: application/json' \
--data '{
"denominationQuantityList": [
{
"denomination": "1.00",
"quantity": 1
}
]
}'