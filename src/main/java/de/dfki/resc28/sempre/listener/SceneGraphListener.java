package de.dfki.resc28.sempre.listener;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import de.dfki.resc28.LDrawParser.LDrawParser.MatrixContext;
import de.dfki.resc28.LDrawParser.LDrawParser.PointContext;
import de.dfki.resc28.LDrawParser.LDrawParser.Reference_rowContext;
import de.dfki.resc28.LDrawParser.LDrawParserBaseListener;
import de.dfki.resc28.sempre.Server;
import de.dfki.resc28.sempre.Util;
import de.dfki.resc28.sempre.vocabularies.ADMS;
import de.dfki.resc28.sempre.vocabularies.MATHS;
import de.dfki.resc28.sempre.vocabularies.SCENEGRAPH;
import de.dfki.resc28.sempre.vocabularies.SPATIAL;
import de.dfki.resc28.sempre.vocabularies.VOM;


public class SceneGraphListener extends LDrawParserBaseListener
{

	private Model sceneGraphModel;
	
	private String sceneGraphUri;
	private Resource sceneGraphRoot;
	private Resource sceneGraphRootCS;
	
	public SceneGraphListener(String sceneGraphUri)
	{
		super();
		
		this.sceneGraphUri = sceneGraphUri;
		
		
		this.sceneGraphModel = ModelFactory.createDefaultModel();
		sceneGraphModel.setNsPrefix("rdf", RDF.getURI());
		sceneGraphModel.setNsPrefix("rdfs", RDFS.getURI());
		sceneGraphModel.setNsPrefix("xsd", XSD.NS);
		sceneGraphModel.setNsPrefix("ldraw", "http://www.ldraw.org/ns/ldraw#");
		sceneGraphModel.setNsPrefixes(ADMS.NAMESPACE);
		sceneGraphModel.setNsPrefix("vom", "http://vocab.arvida.de/2015/06/vom/vocab#");	
		sceneGraphModel.setNsPrefix("maths", "http://vocab.arvida.de/2015/06/maths/vocab#");	
		sceneGraphModel.setNsPrefix("spatial", "http://vocab.arvida.de/2015/06/spatial/vocab#");
		sceneGraphModel.setNsPrefix("material", "http://cluster-frontend/storage/ns/colors#");
		sceneGraphModel.setNsPrefix("vis", "http://vocab.arvida.de/2015/06/visService/vocab#");
		sceneGraphModel.setNsPrefix("sg", "http://vocab.arvida.de/2015/06/scenegraph/vocab#");

		this.sceneGraphRoot = sceneGraphModel.createResource(this.sceneGraphUri);
		this.sceneGraphRootCS = sceneGraphModel.createResource();
		
		this.sceneGraphModel.add(sceneGraphRootCS, RDF.type, MATHS.RightHandedCartesianCoordinateSystem3D);
		this.sceneGraphModel.add(sceneGraphRootCS, MATHS.downAxis, MATHS.y);
		this.sceneGraphModel.add(sceneGraphRootCS, MATHS.axesUnit, this.sceneGraphModel.createResource("http://www.ldraw.org/ns/ldraw#LDU"));
		this.sceneGraphModel.add(sceneGraphRoot, RDF.type, SCENEGRAPH.SceneNode);
		this.sceneGraphModel.add(sceneGraphRoot, RDF.type, SCENEGRAPH.PartOfNode);
		this.sceneGraphModel.add(sceneGraphRoot, SCENEGRAPH.nodeCoordinateSystem, sceneGraphRootCS);
		
		// TODO: this triple should come from serendipity!?
		// TODO: assetUri comes from queryParams in scenegraphURI!!!
		// this.sceneGraphModel.add(sceneGraphRoot, this.sceneGraphModel.createProperty("http://vocab.arvida.de/2015/06/scenegraph/vocab#nodeComponent"), assetXYZ)	
	}
	
	public Model getModel()
	{
		return this.sceneGraphModel;
	}
	
	@Override
	public void exitReference_row(Reference_rowContext ctx)
	{
		Resource freshSceneNodeCS = sceneGraphModel.createResource(); 
		this.sceneGraphModel.add(freshSceneNodeCS, RDF.type, MATHS.RightHandedCartesianCoordinateSystem3D);
		this.sceneGraphModel.add(freshSceneNodeCS, MATHS.downAxis, MATHS.y);
		this.sceneGraphModel.add(freshSceneNodeCS, MATHS.axesUnit, this.sceneGraphModel.createResource("http://www.ldraw.org/ns/ldraw#LDU"));
		
		Resource freshSceneNode = sceneGraphModel.createResource();
		this.sceneGraphModel.add(freshSceneNode, RDF.type, SCENEGRAPH.SceneNode);
		this.sceneGraphModel.add(freshSceneNode, RDF.type, SCENEGRAPH.PartOfNode);
		this.sceneGraphModel.add(freshSceneNode, SCENEGRAPH.nodeCoordinateSystem, freshSceneNodeCS);
		// FIXME
		//		this.sceneGraphModel.add(freshSceneNode, SCENEGRAPH.partOfpartOfParent, this.sceneGraphRoot);
		this.sceneGraphModel.add(this.sceneGraphRoot, SCENEGRAPH.partOf, freshSceneNode);

		MatrixContext rotCtx = ctx.matrix();
		Resource rotationQV = sceneGraphModel.createResource();
		this.sceneGraphModel.add(rotationQV, RDF.type, MATHS.Matrix3D);
		this.sceneGraphModel.add(rotationQV, MATHS.a11, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(0).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a12, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(3).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a13, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(6).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a21, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(1).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a22, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(4).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a23, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(7).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a31, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(2).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a32, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(5).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(rotationQV, MATHS.a33, this.sceneGraphModel.createTypedLiteral(rotCtx.getChild(8).getText(), XSDDatatype.XSDdouble));
		
		Resource rotation = sceneGraphModel.createResource();
		this.sceneGraphModel.add(rotation, RDF.type, SPATIAL.Rotation3D);
		this.sceneGraphModel.add(rotation, VOM.quantityValue, rotationQV);
		
		PointContext transCtx = ctx.point();
		Resource translationQV = sceneGraphModel.createResource();
		this.sceneGraphModel.add(translationQV, RDF.type, MATHS.Vector3D);
		this.sceneGraphModel.add(translationQV, MATHS.x, this.sceneGraphModel.createTypedLiteral(transCtx.getChild(0).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(translationQV, MATHS.y, this.sceneGraphModel.createTypedLiteral(transCtx.getChild(1).getText(), XSDDatatype.XSDdouble));
		this.sceneGraphModel.add(translationQV, MATHS.z, this.sceneGraphModel.createTypedLiteral(transCtx.getChild(2).getText(), XSDDatatype.XSDdouble));
		
		Resource translation = sceneGraphModel.createResource();
		this.sceneGraphModel.add(translation, RDF.type, SPATIAL.Translation3D);
		this.sceneGraphModel.add(translation, VOM.quantityValue, translationQV);
				
		Resource freshSceneNodeSpatialRelation = sceneGraphModel.createResource();
		this.sceneGraphModel.add(freshSceneNodeSpatialRelation, RDF.type, SPATIAL.SpatialRelationship);
		this.sceneGraphModel.add(freshSceneNodeSpatialRelation, SPATIAL.sourceCoordinateSystem, this.sceneGraphRootCS);
		this.sceneGraphModel.add(freshSceneNodeSpatialRelation, SPATIAL.targetCoordinateSystem, freshSceneNodeCS); 
		this.sceneGraphModel.add(freshSceneNodeSpatialRelation, SPATIAL.rotation, rotation);
		this.sceneGraphModel.add(freshSceneNodeSpatialRelation, SPATIAL.translation, translation);
		
		// TODO: hard-coded asset reference?
		Resource freshSceneNodeAsset;
		
		if (ctx.subFile() != null)
			freshSceneNodeAsset = sceneGraphModel.createResource(Util.appendSegmentToPath(Server.fOleURI, "repo/assets/" + FilenameUtils.getBaseName(ctx.subFile().getText())));
		else if (ctx.subPart() != null)
			freshSceneNodeAsset = sceneGraphModel.createResource(Util.appendSegmentToPath(Server.fOleURI, "repo/assets/" + FilenameUtils.getBaseName(ctx.subPart().getText())));
		else 
			freshSceneNodeAsset = sceneGraphModel.createResource();
		
		
		this.sceneGraphModel.add(freshSceneNode, SCENEGRAPH.nodeComponent, freshSceneNodeAsset);
		this.sceneGraphModel.add(freshSceneNodeAsset, RDF.type, SCENEGRAPH.Component);
		this.sceneGraphModel.add(freshSceneNodeAsset, RDF.type, ADMS.Asset);
		

		
		if (ctx.colour() != null)
		{
			Resource freshSceneNodeColour = sceneGraphModel.createResource(Util.appendSegmentToPath("http://cluster-frontend/storage/ns/colors#", ctx.colour().getText()));
			this.sceneGraphModel.add(freshSceneNode, SCENEGRAPH.material, freshSceneNodeColour);
		}
		
	}
}