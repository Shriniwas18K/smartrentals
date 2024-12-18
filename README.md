# Properties - CRUD

Below is structure of codebase
<pre> 
    backend.properties_crud
    |-config
    |   |- security - contains all spring security related stuff session based authentication
    |   |- custom - contains all custom filters for request response interception
    |- controllers
    |   |- handle only request level validation using DTOs
    |   |- calling relevant service methods 
    |   |- returning response to client using DTOs
    |   |- contains public and private directories
    |- DTOs
    |   |- packages are organized based on structure packages in controllers and other exclusive directory "shared"
    |   |- these are shared between controller and service layer
    |   |- DTOs are made for only required endpoints where request body or response body is enough large
    |   |- DTOs are POJOs which are contain the format of request body or stuff expected in response
    |   |- DTOs contain all validation logic for requests in controller layer
    |- services
    |   |- all services implementations are compliant to interfaces
    |   |- packages are based on controllers requiring functionality
    |   |- handle all logic of application
    |   |- consider validated input and return output by DTOs
    |   |- call relevant repository methods
    |- entities
    |   |- contain entity classes
    |- repository
    |   |- contain service layer based packages with clarity in purpose
</pre>