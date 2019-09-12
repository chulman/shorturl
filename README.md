
## ShortURL

> backend
- java 8
- redis
- jpa
- lombok

> frontend
- mustache

### DB Schema  

- SHORT_URL 
    + id
    + link 
    + status 
    + createdAt

### Web

| Method | URI | Action |  
| :------------ | :-----------: | -------------------: | 
| GET | / | index pages  |
| GET | /{code} | redirect original pages |


### REST API

- URI prefix : /api/v1

| Method | URI | Action |  
| :------------ | :-----------: | -------------------: | 
| GET | /short-url/{code} | get short-url with shorten code path  |
| GET | /short-url/get/url?url=... | save and get short-url |

## Branches
### Reactive ShortURL

- rxJava2
- jdbc
- redis reactive
