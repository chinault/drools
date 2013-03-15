package org.drools.decisiontable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.drools.compiler.compiler.DecisionTableFactory;
import org.drools.rule.Rule;
import org.junit.Test;
import org.kie.KnowledgeBase;
import org.kie.KnowledgeBaseFactory;
import org.kie.builder.DecisionTableConfiguration;
import org.kie.builder.DecisionTableInputType;
import org.kie.builder.KnowledgeBuilder;
import org.kie.builder.KnowledgeBuilderFactory;
import org.kie.io.ResourceFactory;
import org.kie.io.ResourceType;

public class ColumnReplaceTest {

	@Test
	public void testAutoFocusToLockOnActiveReplacement () throws FileNotFoundException {
		
        DecisionTableConfiguration dTableConfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dTableConfiguration.setInputType( DecisionTableInputType.CSV );

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("columnReplaceTest.csv", getClass()), ResourceType.DTABLE, dTableConfiguration);
        if (kbuilder.hasErrors())
        {
            System.out.println(kbuilder.getErrors());
            fail("Knowledge builder cannot compile package!");
        }
        System.out.println(DecisionTableFactory.loadFromInputStream(new FileInputStream(new File("src/test/resources/org/drools/decisiontable/columnReplaceTest.csv")), dTableConfiguration));
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        assertTrue(((Rule)kbase.getRule("org.drools.decisiontable", "lockOnActiveRule")).isLockOnActive());
        // lock-on-active was not set on autoFocusRule, so it should be by default false
        assertFalse(((Rule)kbase.getRule("org.drools.decisiontable", "autoFocusRule")).isLockOnActive());
        
        assertFalse(((Rule)kbase.getRule("org.drools.decisiontable", "lockOnActiveRule")).getAutoFocus());
        // auto-focus was set to be true, so it should be true
        assertTrue(((Rule)kbase.getRule("org.drools.decisiontable", "autoFocusRule")).getAutoFocus());
	}
}
