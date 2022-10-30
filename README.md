# generator-recruitment-task
Zadanie rekrutacyjne polegające na stworzeniu serwisu generującego asynchronicznie duże ilości stringów.

W zadaniu wykorzystałem Javę, Spring Boota i bazę H2.

Spakowanego jara można pobrać z zakładki release oraz uruchomić poleceniem

`java -jar generator-recruitment-task-0.0.1-SNAPSHOT.jar`

A testy można uruchomić znajdując się w głównym katalogu poleceniem

`mvn clean test`

Obsługiwane endpointy:

1.
```
POST /api/v1/job - tworzy nowe zlecenie, zwraca obiekt zawierający przydzielone ID zlecenia

Body:
{
    "minLength":1,
    "maxLength":100,
     "chars":["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","r","s","t","u","w","x","y","z","&", "@", "^"],
    "jobSize":1000000
}
```
2.
`GET /api/v1/job/running - pobiera zlecenia które są obecnie wykonywane`

3.
`GET /api/v1/job/{id} - pobiera informacje o zleceniu o zadanym ID`





