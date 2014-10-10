<SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP:Body>
    <acceptOrder xmlns="http://www.webex.com/blis/1.0/SOA/main">
    	<OrderID>${orderid}</OrderID>
    	<CurrentUser>${currentUser}</CurrentUser>
    </acceptOrder>
  </SOAP:Body>
</SOAP:Envelope>