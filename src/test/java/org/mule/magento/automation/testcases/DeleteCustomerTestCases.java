package org.mule.magento.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;

import com.magento.api.CustomerCustomerEntityToCreate;

public class DeleteCustomerTestCases extends MagentoTestParent {

	@Before
	public void setUp() {
		try {
			testObjects = (HashMap<String, Object>) context.getBean("deleteCustomer");

			CustomerCustomerEntityToCreate customer = (CustomerCustomerEntityToCreate) testObjects.get("customer");
			testObjects.put("email", customer.getEmail());
			testObjects.put("password", customer.getPassword());
			testObjects.put("firstname", customer.getFirstname());
			testObjects.put("lastname", customer.getLastname());

			// Create the customer
			MessageProcessor flow = lookupFlowConstruct("create-customer");
			MuleEvent response = flow.process(getTestEvent(testObjects));

			// Get the ID of the created customer
			Integer customerId = (Integer) response.getMessage().getPayload();
			testObjects.put("customerId", customerId);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Category({ SmokeTests.class, RegressionTests.class })
	@Test
	public void testDeleteCustomer() {
		try {
			
			MessageProcessor flow = lookupFlowConstruct("delete-customer");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			
			boolean deleted = (Boolean) response.getMessage().getPayload();
			assertTrue(deleted);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}