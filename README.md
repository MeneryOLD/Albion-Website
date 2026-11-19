# Albion — Language School Website

**Live site: [https://albiononline.biz/](https://albiononline.biz/)**

Albion is the website of a language school. Visitors can browse language courses, read the blog, look at the store, sign up for the newsletter and create an account. Administrators manage all content (courses, articles, store items, pictures and custom page designs) from a built-in admin panel, with no code changes needed.

---

## Tech stack

| Layer      | Technology                                                       |
|------------|------------------------------------------------------------------|
| Backend    | Java 17, Spring Boot 4 (Web MVC, Data JPA, Security, Mail)       |
| Views      | Thymeleaf server-side templates + `static/css/albion.css`        |
| Database   | MySQL 8 (schema managed by Hibernate, `ddl-auto=update`)          |
| Admin UI   | Bootstrap 5, GrapesJS page builder, Quill rich-text editor       |
| Deployment | Docker + Docker Compose                                           |

---

## How the website works

### Public pages

| URL                          | Page                                              |
|------------------------------|---------------------------------------------------|
| `/`                          | Home page                                         |
| `/language-courses`          | List of all courses                               |
| `/language-courses/{slug}`   | A single course                                   |
| `/blog`                      | Blog: list of articles                            |
| `/blog/{slug}`               | A single article                                  |
| `/store`                     | Store: list of items                              |
| `/store/{slug}`              | A single store item                               |
| `/about-us`                  | About the school                                  |
| `/login`, `/register`        | Sign in / create an account                       |
| `/privacy-policy`, `/refund-policy`, `/terms-of-service` | Legal pages           |

Every course, article and store item has a **slug** (a URL-friendly name such as `english-b2`), which forms its public address.

### Custom page designs

Each course, article or item is shown with a **standard page template** by default. An admin can also design a **custom page** for it in the visual page builder. If a custom page is saved, the site shows it instead of the standard one. If the custom page is emptied (or "Standard page" is clicked in the builder), the site falls back to the standard template automatically.

### Users and roles

- **Visitors** can browse everything public.
- **Registered users** (`USER`) can sign in and see their profile.
- **Administrators** (`ADMIN`) also see an **Admin panel** link in the profile menu and can access `/admin/stats` and every `/api/admin/**` endpoint.

Passwords are stored hashed with BCrypt.

### Newsletter

Visitors can subscribe or unsubscribe through `/api/newsletter/subscribe` and `/api/newsletter/unsubscribe`. Emails are sent over SMTP using the `MAIL_*` settings.

### Pictures

Pictures for courses, articles and items are uploaded in the admin panel and stored in the database. They are served from `/api/pictures/...`.

---

## Admin panel

Open **`/admin/stats`** while signed in as an admin, or use the **Admin panel** link in the profile menu.

- **Courses / Articles / Store items:** create, edit and delete entries; search and filter them (Live / Drafts); upload pictures. The slug is filled in automatically from the title.
- **Page builder:**
  - Pick an entry and its page loads automatically.
  - Start from a ready-made **template**, or drag in **Albion sections**: hero, text, image + text, features, steps, FAQ, gallery, call-to-action and more.
  - Press **Save** (or Ctrl+S). The builder warns you before you leave with unsaved changes.
  - Click **Standard page** to go back to the default design.
  - The preview link opens the live page.

### Creating the first admin account

Set these environment variables before starting the app:

```
ADMIN_USERNAME=admin
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=a-strong-password
```

On startup, the app creates this account, or promotes an existing user with that name to `ADMIN`. If the variables are empty, nothing happens. When using Docker Compose, also add them to the `environment:` section of the `app` service.

---

## Project structure

```
src/main/java/com/albion/website/
├── config/       Security, web config, admin account initializer
├── controller/   Public pages, auth, admin REST API, pictures, newsletter
├── dto/          Data transfer objects for views and the API
├── model/        JPA entities: Course, Article, Item, Picture, User, Subscriber…
├── repository/   Spring Data JPA repositories
└── service/      Business logic (courses, articles, items, email, slugs, pictures)

src/main/resources/
├── templates/    Thymeleaf pages (home, course, blog, store, admin, …)
├── static/       CSS (albion.css design system), JS, images
└── application.properties
```

---

## Running locally

**Requirements:** Java 17+ and MySQL 8, or just Docker.

### With Docker (recommended)

1. Copy `.env.example` to `.env` and fill in the values (database, mail, optional admin account).
2. Start everything:
   ```bash
   docker compose up -d --build
   ```
3. Open [http://localhost:8080](http://localhost:8080).

### Without Docker

1. Start MySQL and create a database.
2. Set the environment variables from `.env.example`, plus `DB_URL`, e.g. `jdbc:mysql://localhost:3306/albion_db`.
3. Run:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Deploying updates

The production server runs the project with Docker Compose. To deploy a new version:

```bash
cd ~/app
git pull origin main
docker compose up -d --build
```

Check the logs with `docker compose logs -f app`.

---

## Configuration

| Variable              | Purpose                                  |
|-----------------------|------------------------------------------|
| `MYSQL_ROOT_PASSWORD` | MySQL root password (Docker)             |
| `MYSQL_DATABASE`      | Database name                            |
| `DB_URL`              | JDBC URL (set automatically in Docker)   |
| `DB_USERNAME` / `DB_PASSWORD` | Database credentials             |
| `MAIL_HOST` / `MAIL_PORT` / `MAIL_USERNAME` / `MAIL_PASSWORD` | SMTP settings for emails |
| `ADMIN_USERNAME` / `ADMIN_EMAIL` / `ADMIN_PASSWORD` | Optional admin account created on startup |

Never commit the real `.env` file.
