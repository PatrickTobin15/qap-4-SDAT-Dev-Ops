# Golf Club Membership & Tournament API

This is my QAP for Software Design, Architecture, and Testing/DevOps. It's a Spring Boot REST API for a golf club that tracks members and the tournaments they register for. It talks straight to a MySQL database (no in-memory storage).

## What it does

- Add and retrieve Members
- Add and retrieve Tournaments
- Register a Member to a Tournament
- Search Members by name, membership type, phone number, or tournament start date
- Search Tournaments by start date or location

## Data model

**Member**: id, member name, address, email, phone number, membership start date, membership type which is Annual Monthly or LifeTime

**Tournament**: id, start date, end date, location, entry fee, cash prize amount, and a list of participating members.

It's a many to many relationship between Members and Tournaments (a member can join more than one tournament, and a tournament has more than one member), so there is a join table that is called `tournament_members` that Hibernate creates automatically.

## Endpoints

### Members
| Method | Endpoint | What it does |
|---|---|---|
| POST | `/api/members` | Add a member |
| GET | `/api/members` | Get all members |
| GET | `/api/members/{id}` | Get one member by id |
| PUT | `/api/members/{id}` | Update a member |
| DELETE | `/api/members/{id}` | Delete a member |
| GET | `/api/members/search/name?name=` | Search by name (partial match, case-insensitive) |
| GET | `/api/members/search/type?type=` | Search by membership type |
| GET | `/api/members/search/phone?phone=` | Search by phone number (partial match) |
| GET | `/api/members/search/tournament-start-date?date=YYYY-MM-DD` | Find members registered to a tournament starting on that date |

### Tournaments
| Method | Endpoint | What it does |
|---|---|---|
| POST | `/api/tournaments` | Add a tournament |
| GET | `/api/tournaments` | Get all tournaments |
| GET | `/api/tournaments/{id}` | Get one tournament by id |
| PUT | `/api/tournaments/{id}` | Update a tournament |
| DELETE | `/api/tournaments/{id}` | Delete a tournament |
| POST | `/api/tournaments/{tournamentId}/register/{memberId}` | Register a member to a tournament |
| GET | `/api/tournaments/search/start-date?date=YYYY-MM-DD` | Search by start date |
| GET | `/api/tournaments/search/location?location=` | Search by location (partial match) |

### Example request bodies

Adding a member:
```json
{
  "memberName": "Sam Wilson",
  "memberAddress": "12 Fairway Lane",
  "memberEmailAddress": "sam.wilson@email.com",
  "memberPhoneNumber": "709-555-0123",
  "membershipStartDate": "2026-01-15",
  "membershipType": "Annual"
}
```

Adding a tournament:
```json
{
  "startDate": "2026-08-10",
  "endDate": "2026-08-12",
  "location": "Clovelly Golf Course",
  "entryFee": 75.00,
  "cashPrizeAmount": 1500.00
}
```

I tested every one of these in Postman — screenshots are in the `/postman-screenshots` folder (added separately, not part of the code push).

## Running it locally without Docker

1. Have MySQL running locally and update `src/main/resources/application.properties` if your credentials aren't the defaults (`root`/`root`).
2. `mvn clean install`
3. `mvn spring-boot:run`
4. API is at `http://localhost:8080`

## Running it in Docker

This is the easiest way to run the whole thing — no manual MySQL setup needed.

1. Make sure Docker Desktop is running.
2. From the project root:
   ```
   docker compose up --build
   ```
3. That spins up two containers: a MySQL container and the API container. The API waits for MySQL to actually be healthy before it starts, so I don't get the classic "connection refused because MySQL wasn't ready yet" error.
4. API is at `http://localhost:8080`.

To stop everything: `docker compose down` (add `-v` if you also want to wipe the database volume).

## Connecting to RDS

For the cloud deployment part, the app doesn't have RDS hardcoded it reads the DB connection from environment variables, so pointing it at RDS instead of local MySQL is just a matter of setting different values. There's an `application-prod.properties` profile set up for this.

Steps I followed:
1. Created a MySQL RDS instance in AWS (free tier, `db.t3.micro`).
2. Set the RDS security group to allow inbound traffic on port 3306 from my IP (and from the app if it's running somewhere else in AWS).
3. Ran the container with the prod profile active and the RDS values passed in as environment variables:
   ```
   docker run -p 8080:8080 \
     -e SPRING_PROFILES_ACTIVE=prod \
     -e DB_HOST=<your-rds-endpoint> \
     -e DB_PORT=3306 \
     -e DB_NAME=golfclub \
     -e DB_USERNAME=<your-username> \
     -e DB_PASSWORD=<your-password> \
     golf-club-api
   ```
4. Confirmed the connection by hitting `GET /api/members` and checking the tables actually got created in the RDS instance (Hibernate `ddl-auto=update` handles that part).

**Honest note:** I got the connection working from my PC to the RDS instance. I did not get the full app deployed to something like EC2/Elastic Beanstalk in the time I had that's the part I'd tackle next if I kept going. Screenshots of the RDS setup and the connection test are in `/deployment-screenshots`.

## Issues I ran into

- Docker was not installed on my PC at all when I first tried docker compose up --build — got a "docker not recognized" error. Installed Docker Desktop, but it then failed to start with "Virtualization support not detected" even though my CPU supports it. Turned out Windows wasn't passing virtualization through to Hyper-V/WSL2 at boot which was fixed by running bcdedit /set hypervisorlaunchtype auto in an elevated (Admin) PowerShell window and restarting the whole PC. First attempt at this failed with "Access is denied" because I hadn't actually opened PowerShell as Administrator.
  
- Sent a search request in Postman as POST instead of GET out of habit and got a 405 Method Not Allowed  all the search/filter endpoints are GET requests since they're just retrieving data, not creating anything.
  
- When connecting to the RDS docker run tried to pull golf-club-api from Docker Hub instead of using the image I'd already built locally, since Docker Compose had actually tagged it golf-club-api-api:latest and not the bare name I checked docker images to find the real tag and used that instead.

## Optional CI/CD

There's a GitHub Actions workflow at `.github/workflows/docker-publish.yml` that builds the Docker image and pushes it to Docker Hub, but only on a merge to `master` (not on every PR). To actually use it you'd need to add `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` as repo secrets. I included the file since I had it, but I didn't wire up real Docker Hub secrets for this submission  it's there just to show the pattern.
