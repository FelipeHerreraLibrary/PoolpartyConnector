# PoolpartyConnector

The Project aim at providing a content repository connector for The PoolParty Thesaurus Server.

It allows to make use of the several Services offered by PoolParty e.g. Content Categorization,
Content Enrichment, and so son
It also provides a Service to Cache the entire thesaurus with automated update for fast lookup
Although it has specific features for the Dspace content repository, given that it is the original system with which it is developped,
care has been taken to separate it away from the specific functionalities of dspace
In the future both part will be pushed in different sub-projects, thereby making this project a PoolParty connector for any CMS.

The project is being developped in Scala. It uses Spray, Akka, and all other scala goodies :)
