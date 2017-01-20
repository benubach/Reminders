
# Reminders

How to start the Reminders application
---
1. Update the file `src/main/resources/reminders.yml` with the recpient of the alerts and your gmail credentials (you may need to allow 'less secure applications' like I did)
2. Run `mvn clean install` to build your application
3. Create the database with the following command:
 `java -jar target/Reminders-1.0-SNAPSHOT.jar db migrate ./src/main/resources/reminders.yml`
4. Start application with `java -jar target/Reminders-1.0-SNAPSHOT.jar server ./src/main/resources/reminders.yml`
5. To get a json view of all reminders, go to `http://localhost:8080/reminder`
6. To create reminders, post a JSON string to `http://localhost:8080/reminder` like the following examples:

To create reminders specifying all fiends
```json
{"content":"Test 1","status":"PENDING", "dueDate": "2017-01-20 03:43"}
```
or
```json
{"content":"Test 2","status":"DONE", "dueDate": "2017-01-20 03:43"}
```
To create a reminder with the specified status and a 1 week due date

```json
{"content":"Test 3","status":"DONE"}
```

And to create a reminder with status "PENDING" and a 1 week due date only send the content
```json
{"content":"Test 4"}
```

7. Go to the url `http://localhost:8080/frontend/reminders.html` to get a better view of the reminders list

