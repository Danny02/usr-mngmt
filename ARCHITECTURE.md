# Overview

The Role-Service is the main application, it holds all state. Both User-Ui and Log-Service query their required state.
It would be interesting to explore a persistent log (like kafka) to be able to make both services more independent.
The Log-Service does not need to be as reliable as the Role-Service, therefor it is acceptable to rely on the uptime 
of the Role-Service.