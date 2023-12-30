### Divina Comedia API

I am a huge fan of the classic Dante's story. For this reason I started to playing with data I found online to build this programming software.

This project contains the enviroment to provides a very simple web API that offers information about the calssical/epic story _"Divine Comedy"_, by Dante Alighieri, being those information in prose style. Currently only _brazilian portuguese_ is supported, once the root data is being retrieved from https://stelle.com.br/.

Also, is important to know that all the texts here was developed by Helder da Rocha, the man who adapted the story from poem text style to prose. In the future we should to finish the last book (paradise) and include english versions. Also, we should to offer original poem versions, as well.

The API itself is not online to be accessed over the internet (yet). But this project can run under a device that has [Docker](https://www.docker.com/) installed. Also, if you have the [Gradle tool](https://gradle.org/) installed you can run the helper script [flow.bat](flow.bat), which can run everything needed to this API becomes UP under the URL `http://127.0.0.1:9999/`.

Bellow will be listed a brief description about the API endpoints. Remember taht all of this project is in development, including this presentation.

### Get Books

**Endpoint:** `GET /books`

**Description**:
Retrieve a list of books from the database.

**Example:**
```html
GET /books
```

**Response:**
- **Status Code:** `200 OK`
- **Body:** List of books and their chapters and their notes

### Get Book by Name

**Endpoint:** `GET /book_by_name`

**Description:**
Retrieve information about a specific book by its name from the database.

**Example:**
```html
GET /book_by_name?book_name=inferno
```

**Parameters:**
- `book_name` (string): Name of the book

**Response:**
- **Status Code:** `200 OK`
- **Body:** Book details

### Get Chapter by Book Name and Chapter Order

**Endpoint:** `GET /chapter_by_book_name_and_order`

**Description:**
Retrieve information from the database about a specific chapter of a book by its name and its order.

**Example:**
```html
GET /chapter_by_book_name_and_order?book_name=inferno&order=1
```

**Parameters:**
- `book_name` (string): Name of the book
- `order` (integer): Order of the chapter inside the passed book

**Response:**
- **Status Code:** `200 OK`
- **Body:** Chapter details

_Note: If the chapter is not found, the response will be 404 Not Found._

### Get Notes by Book and Chapter

**Endpoint:** `GET /notes_by_book_and_chapter`

**Description:**
Retrieve from the database notes for a specific chapter of a book by its name and order.

**Example:**
```html
GET /notes_by_book_and_chapter?book_name=inferno&chapter_order=1
```

**Parameters:**
- `book_name` (string): Name of the book
- `chapter_order` (integer): Order of the chapter

**Response:**
- **Status Code:** 200 OK
- **Body:** Notes details

_Note: If the notes are not found, the response will be 404 Not Found._

### Error Handling

- If there is an internal server error, the response will be 500 Internal Server Error.
- If there are missing or invalid parameters, the response will be 400 Bad Request.