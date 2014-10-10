<SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP:Body>
    <showCiscoOrderNumber xmlns="http://www.webex.com/blis/1.0/SOA/main">
    	<entityId>${entityId?if_exists}</entityId>
    	<entityKeyId>${entityKeyId?if_exists}</entityKeyId>
    </showCiscoOrderNumber>
  </SOAP:Body>
</SOAP:Envelope>