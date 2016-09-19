/**
 * This file has been automatically generated using Grover (https://github.com/rmrschub/grover).
 * It contains static constants for the terms in the SCENEGRAPH vocabulary.
 */
package de.dfki.resc28.sempre.vocabularies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;

public final class SCENEGRAPH 
{
  public static final String PREFIX = "SCENEGRAPH";
  public static final PrefixMapping NAMESPACE = PrefixMapping.Factory.create().setNsPrefix(PREFIX, CONSTANTS.NS);

  /** 
   * Classes as org.apache.jena.rdf.model.Resource
   */
  public static final Resource TransformationGroupNode = resource(CONSTANTS.CLASS_TransformationGroupNode);
  public static final Resource BoundingVolumeComponent = resource(CONSTANTS.CLASS_BoundingVolumeComponent);
  public static final Resource ComponentOverwrite = resource(CONSTANTS.CLASS_ComponentOverwrite);
  public static final Resource RenderComponent = resource(CONSTANTS.CLASS_RenderComponent);
  public static final Resource Component = resource(CONSTANTS.CLASS_Component);
  public static final Resource PartOfNode = resource(CONSTANTS.CLASS_PartOfNode);
  public static final Resource AssetComponent = resource(CONSTANTS.CLASS_AssetComponent);
  public static final Resource SceneNode = resource(CONSTANTS.CLASS_SceneNode);

  /** 
   * Properties as org.apache.jena.rdf.model.Property
   */
  public static final Property renderedGroupChild = property(CONSTANTS.PROP_renderedGroupChild);
  public static final Property overwrittenProperty = property(CONSTANTS.PROP_overwrittenProperty);
  public static final Property propagates = property(CONSTANTS.PROP_propagates);
  public static final Property transformationGroupChild = property(CONSTANTS.PROP_transformationGroupChild);
  public static final Property transformationGroupParentOf = property(CONSTANTS.PROP_transformationGroupParentOf);
  public static final Property overwrite = property(CONSTANTS.PROP_overwrite);
  public static final Property targetClass = property(CONSTANTS.PROP_targetClass);
  public static final Property valueNode = property(CONSTANTS.PROP_valueNode);
  public static final Property nodeCoordinateSystem = property(CONSTANTS.PROP_nodeCoordinateSystem);
  public static final Property componentNode = property(CONSTANTS.PROP_componentNode);
  public static final Property renderedGroupParent = property(CONSTANTS.PROP_renderedGroupParent);
  public static final Property asset = property(CONSTANTS.PROP_asset);
  public static final Property transformationGroupParent = property(CONSTANTS.PROP_transformationGroupParent);
  public static final Property boundingVolume = property(CONSTANTS.PROP_boundingVolume);
  public static final Property renderableGroupParentOf = property(CONSTANTS.PROP_renderableGroupParentOf);
  public static final Property partOf = property(CONSTANTS.PROP_partOf);
  public static final Property materialOverwriteGroupParent = property(CONSTANTS.PROP_materialOverwriteGroupParent);
  public static final Property nodeComponent = property(CONSTANTS.PROP_nodeComponent);
  public static final Property rendered = property(CONSTANTS.PROP_rendered);
  public static final Property materialOverwriteGroupChild = property(CONSTANTS.PROP_materialOverwriteGroupChild);
  public static final Property materialOverwriteGroupParentOf = property(CONSTANTS.PROP_materialOverwriteGroupParentOf);


  /**
   * Returns a Jena resource for the given namespace name 
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Resource
   */
  private static final Resource resource(String nsName)
  {
    return ResourceFactory.createResource(nsName); 
  }

  /**
   * Returns a Jena property for the given namespace name
   * @param nsName  the full namespace name of a vocabulary element as a string
   * @return the vocabulary element with given namespace name as a org.apache.jena.rdf.model.Property
   */
  private static final Property property(String nsName)
  { 
    return ResourceFactory.createProperty(nsName);
  }

  private static final class CONSTANTS 
  {
    /**
     * Vocabulary namespace URI as string 
     */
    private static final String NS = "http://vocab.arvida.de/2015/06/scenegraph/vocab";

    /**
     * Local and namespace names of RDF(S) classes as strings 
     */
    private static final String CLASS_LNAME_TransformationGroupNode = "TransformationGroupNode";
    private static final String CLASS_TransformationGroupNode = nsName(CLASS_LNAME_TransformationGroupNode);
    private static final String CLASS_LNAME_BoundingVolumeComponent = "BoundingVolumeComponent";
    private static final String CLASS_BoundingVolumeComponent = nsName(CLASS_LNAME_BoundingVolumeComponent);
    private static final String CLASS_LNAME_ComponentOverwrite = "ComponentOverwrite";
    private static final String CLASS_ComponentOverwrite = nsName(CLASS_LNAME_ComponentOverwrite);
    private static final String CLASS_LNAME_RenderComponent = "RenderComponent";
    private static final String CLASS_RenderComponent = nsName(CLASS_LNAME_RenderComponent);
    private static final String CLASS_LNAME_Component = "Component";
    private static final String CLASS_Component = nsName(CLASS_LNAME_Component);
    private static final String CLASS_LNAME_PartOfNode = "PartOfNode";
    private static final String CLASS_PartOfNode = nsName(CLASS_LNAME_PartOfNode);
    private static final String CLASS_LNAME_AssetComponent = "AssetComponent";
    private static final String CLASS_AssetComponent = nsName(CLASS_LNAME_AssetComponent);
    private static final String CLASS_LNAME_SceneNode = "SceneNode";
    private static final String CLASS_SceneNode = nsName(CLASS_LNAME_SceneNode);

    /**
     * Local and namespace names of RDF(S) properties as strings 
     */
    private static final String PROP_LNAME_renderedGroupChild = "renderedGroupChild";
    private static final String PROP_renderedGroupChild = nsName(PROP_LNAME_renderedGroupChild);
    private static final String PROP_LNAME_overwrittenProperty = "overwrittenProperty";
    private static final String PROP_overwrittenProperty = nsName(PROP_LNAME_overwrittenProperty);
    private static final String PROP_LNAME_propagates = "propagates";
    private static final String PROP_propagates = nsName(PROP_LNAME_propagates);
    private static final String PROP_LNAME_transformationGroupChild = "transformationGroupChild";
    private static final String PROP_transformationGroupChild = nsName(PROP_LNAME_transformationGroupChild);
    private static final String PROP_LNAME_transformationGroupParentOf = "transformationGroupParentOf";
    private static final String PROP_transformationGroupParentOf = nsName(PROP_LNAME_transformationGroupParentOf);
    private static final String PROP_LNAME_overwrite = "overwrite";
    private static final String PROP_overwrite = nsName(PROP_LNAME_overwrite);
    private static final String PROP_LNAME_targetClass = "targetClass";
    private static final String PROP_targetClass = nsName(PROP_LNAME_targetClass);
    private static final String PROP_LNAME_valueNode = "valueNode";
    private static final String PROP_valueNode = nsName(PROP_LNAME_valueNode);
    private static final String PROP_LNAME_nodeCoordinateSystem = "nodeCoordinateSystem";
    private static final String PROP_nodeCoordinateSystem = nsName(PROP_LNAME_nodeCoordinateSystem);
    private static final String PROP_LNAME_componentNode = "componentNode";
    private static final String PROP_componentNode = nsName(PROP_LNAME_componentNode);
    private static final String PROP_LNAME_renderedGroupParent = "renderedGroupParent";
    private static final String PROP_renderedGroupParent = nsName(PROP_LNAME_renderedGroupParent);
    private static final String PROP_LNAME_asset = "asset";
    private static final String PROP_asset = nsName(PROP_LNAME_asset);
    private static final String PROP_LNAME_transformationGroupParent = "transformationGroupParent";
    private static final String PROP_transformationGroupParent = nsName(PROP_LNAME_transformationGroupParent);
    private static final String PROP_LNAME_boundingVolume = "boundingVolume";
    private static final String PROP_boundingVolume = nsName(PROP_LNAME_boundingVolume);
    private static final String PROP_LNAME_renderableGroupParentOf = "renderableGroupParentOf";
    private static final String PROP_renderableGroupParentOf = nsName(PROP_LNAME_renderableGroupParentOf);
    private static final String PROP_LNAME_partOf = "partOf";
    private static final String PROP_partOf = nsName(PROP_LNAME_partOf);
    private static final String PROP_LNAME_materialOverwriteGroupParent = "materialOverwriteGroupParent";
    private static final String PROP_materialOverwriteGroupParent = nsName(PROP_LNAME_materialOverwriteGroupParent);
    private static final String PROP_LNAME_nodeComponent = "nodeComponent";
    private static final String PROP_nodeComponent = nsName(PROP_LNAME_nodeComponent);
    private static final String PROP_LNAME_rendered = "rendered";
    private static final String PROP_rendered = nsName(PROP_LNAME_rendered);
    private static final String PROP_LNAME_materialOverwriteGroupChild = "materialOverwriteGroupChild";
    private static final String PROP_materialOverwriteGroupChild = nsName(PROP_LNAME_materialOverwriteGroupChild);
    private static final String PROP_LNAME_materialOverwriteGroupParentOf = "materialOverwriteGroupParentOf";
    private static final String PROP_materialOverwriteGroupParentOf = nsName(PROP_LNAME_materialOverwriteGroupParentOf);

    /**
     * Returns the full namespace name of a vocabulary element as a string
     * @param localName  the local name of a vocabulary element as a string
     * @return the full namespace name of a vocabulary element as a string
     */
    private static String nsName(String localName) 
    {
      return NS + localName;
    }
  }
}