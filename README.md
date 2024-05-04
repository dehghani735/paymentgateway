# Payment Gateway for Snapppay!

# Overview
A purchase order will have multiple states in its lifecycle.
For simplicity, we have just created and verified states.

we have two APIs, create purchase and update purchase.

## Create purchase

Creates a new purchase and insert it in database.

## Verify purchase

Verifies the purchase if it exists and updates it.

# Stack

In this project, I used postgres as RDBMS. I used jooq in order to interact with database and create entities.

# Installation and Usage

For running project, please first run the following command:

`mvn clean compile`

Then, go to target/generated-sources/jooq and make this directory as "Generated Sources Root".
