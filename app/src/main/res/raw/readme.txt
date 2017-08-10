Android Development | Candidate Task

The task :
We need an application to make reservations in a restaurant.

Workflow:
The waiter opens the app, chooses a customer from a list, the app navigates him to a table choosing screen.
The waiter chooses the table and the app highlights the chosen table.
 Every 10 minutes all reservations have to be removed even if the application is closed.

API description:
Each customer has First Name, Last Name and Id.
The tables are represented as one-dimensional array of booleans where “true” means that the table is available and “false” that it is not.
URL to customers list:  https://s3-eu-west-1.amazonaws.com/q-assessment/customer-list.json URL to tables list:  https://s3-eu-west-1.amazonaws.com/q-assessment/table-map.json

Requirement:
1. Tables have to be represented as cells on grid
2. Available and unavailable tables have to be easily recognized
3. The app has to also work offline
4. Search option for customers would be a plus
5. Any unspecified details are left to your imagination

What we will review:
1. Unit and instrumentation test coverage
2. Architecture
3. Code style
4. Code extensibility
5. Performance

