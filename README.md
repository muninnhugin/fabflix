[Demo video](https://youtu.be/jjIteTzxjOE)

All contribution made by Ha Bach (bachh1).

### Fuzzy Search Implementation
For autocomplete, web search, and Android search, fuzzy search is implement as follow:
##### SQL Pattern Matching
All queries are broken into tokens, each token are matched as `%token%` in SQL.
So for query "good u", the servlets ask the database for `%good%u%`
##### LEDA using the Flamingo library
Since the autocomplete will deal with short queries, edit distance for edth is set as 1 to avoid results that are too different from the query.

For web search and Android search, edit distance is set as 2 because queries are assumed to be close to what the user wants to search for.
##### Soundex
The soundex of the entire query is match against the database's movie titles.
