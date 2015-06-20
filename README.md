# PoolpartyConnector

The Project aim sat providing a content repository connector for The PoolParty Thesaurus Server.

It allows to make use of the several Services offered by PoolParty e.g. Content Categorization, Content Enrichment, and so son

It also provides a Service to Cache the entire thesaurus with automated update for fast lookup


Although it has specific features for the Dspace content repository, as it was the original system with which it was developped,
care has been taken to separate it away from the generic functionality that any content repository may make use of.

In the future both part will be pushed in different sub-projects, thereby making this project a connector for multiple content repository integration.

The user will only have to reference the core lib and one specific to its content repository for integration.


The project is being developped in Scala. It uses Spray, Akka, and all other scala goodies :)
