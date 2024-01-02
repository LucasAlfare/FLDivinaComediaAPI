# Divina Comedia API

I am a huge fan of the classic Dante's story. For this reason I started to playing with data I found online to build this programming software.

This project contains the enviroment to provides a very simple web API that offers information about the calssical/epic story _"Divine Comedy"_, by Dante Alighieri, being those information in prose style. Currently only _brazilian portuguese_ is supported, once the root data is being retrieved from https://stelle.com.br/.

Also, is important to know that all the texts here was developed by Helder da Rocha, the man who adapted the story from poem text style to prose. In the future we should to finish the last book (paradise) and include english versions. Also, we should to offer original poem versions, as well.

The API itself is not online to be accessed over the internet (yet) but can run under a device that has [Docker](https://www.docker.com/) installed. You can run the helper script [flow1.bat](flow1.bat), which setup everything needed to put this API UP, under the URL `http://127.0.0.1:9999/`.

The raw data at all is written in portuguese, which is my main language and the default language of the adaptation made by Helder da Rocha. For this reason, at this point, only portuguese is supported. Anyway, we really plan to offer all the content present here in english, as well. However, this should be done after this API becomes a bit more tested in a minimal production environment or so.

# Tech-stack used

This project uses some distinct tools to work:
- `Kotlin` as main programming language;
- `Ktor` as web server;
- `PostgreSQL` as database handler;
- `Gradle` to build and manage dependencies of the Kotlin code;
- `Docker` to download all needed environment and make the application run inside virtual containers.

# Available values

To call the endpoints and supply the URL queries, this API accepts the book names in portuguese:
- first divine comedy book is referenced to the API as "`inferno`";
- second divine comedy book is referenced to the API as "`purgatorio`";
- third divine comedy book is referenced to the API as "`paraiso`".

# Interacting with the API
Bellow will be listed a brief description about the API endpoints. Remember that all of this project is in development, including this presentation.

### Getting books

**Endpoint:** `GET /books`

**Description**:
Retrieve a list of books from the database. The list will include all the chapter of each book and all notes of each chapter, as well.

**Example:**
```curl
curl http://127.0.0.1:9999/books
```

**Response:**
- **Status Code:**
  - `200 OK` if all the books was successful retrieved from database;
  - `500 Internal Server Error` if something wrong happened when getting all the books.
- **Body:** List of books and their chapters and their notes.

### Getting a book by its name

**Description:**
Retrieve information from the database about a specific book using its name.

**Endpoint:** `GET /book`

**Example:**
```curl
curl http://127.0.0.1:9999/book?name=inferno
```

**Query parameters:**
- `name` (string): Name of the book

**Response:**
- **Status Code:**
  - `200 OK` if found;
  - `404 Not Found` if book was not found with the supplied `name`;
  - `400 Bad Request` if query in the url is invalid or not present.
- **Body:** Book details

### Get Chapter by Book Name and Chapter Order

**Description:**
Retrieve information from the database about a specific chapter of a book by its name and its order.

**Endpoint:** `GET /chapter`

**Example:**
```curl
curl "http://127.0.0.1:9999/chapter?book=purgatorio&order=3"
```

**Parameters:**
- `book` (string): Name of the book
- `order` (integer): Order of the chapter inside the passed book

**Response:**
- **Status Code:**
  - `200 OK` if found;
  - `404 Not Found` if book or chapter was not found with the supplied `book` and `order` query parameters;
  - `400 Bad Request` if query in the url is invalid or not present.
- **Body:** Chapter details

### Get Notes by Book and Chapter

**Description:**
Retrieve from the database notes for a specific chapter of a book by its name and order.

**Endpoint:** `GET /notes`

**Example:**
```curl
curl "http://127.0.0.1:9999/notes?book=paraiso&chapter=2"
```

**Parameters:**
- `book` (string): Name of the book
- `chapter` (integer): Order of the chapter

**Response:**
- **Status Code:**
  - `200 OK` if found, even if the notes list is empty;
  - `400 Bad Request` if query in the url is invalid or not present.
- **Body:** Notes details

# Next steps

This API is very roughly finished. For this reason we expect to improve all the things done at this point, but we also plan to implement other stuff, such as:
- Include texts in poem style, making possible to choose retrieve the chapter in prose or poem text styles;
- Include english versions of every content (chapters content and notes content);
- Integrate the "chapter title" in the tables and data models. This should make possible to create other useful end points, such as "search something by presence in title".

# Contributing

We encourage everyone to contribute in this project. Even this is a personal study, in the end it should be useful for those who likes the Dante's story, too. For this reason, any contribution is appreciated.

Also, as the project is easily build on top of Docker containers is very easy to clone and setup local development, making very cool the contribution initiation.