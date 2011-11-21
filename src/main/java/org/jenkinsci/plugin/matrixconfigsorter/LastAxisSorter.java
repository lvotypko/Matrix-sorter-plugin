package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.Axis;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.util.FormValidation;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Sort configurations {@link MatrixConfiguration}  of matrix job {@link MatrixProject} by order of values in last axis.
 * 
 * @author Lucie Votypkova
 */
@Extension
public class LastAxisSorter extends MatrixConfigurationSorter{

    public int compare(MatrixConfiguration configuration1, MatrixConfiguration configuration2) {
        int compare=0;
        List<Axis> projectAxes = configuration1.getParent().getAxes();
        if(!projectAxes.isEmpty()){
            Axis lastAxis = projectAxes.get(projectAxes.size() - 1);
            List<String> valuesOfLastAxis = lastAxis.getValues();
            compare = compare(valuesOfLastAxis.indexOf(configuration1.getCombination().get(lastAxis.getName())),valuesOfLastAxis.indexOf(configuration2.getCombination().get(lastAxis.getName())));
        }
        else{
            
        }
        if(compare==0){
            compare= configuration1.getDisplayName().compareTo(configuration2.getDisplayName());
        }
        return compare;
    }

    @DataBoundConstructor
    public LastAxisSorter() {
    }
    
    private int compare(int order1, int order2){
        return order1 - order2;
    }

    @Override
    public void validate(MatrixProject p) throws FormValidation {
        if(p.getAxes().size()<1){
            FormValidation.error("Sorting by last axis need at leas one axis");
        }
    }


    @Extension(ordinal=100) // this is the default
    public static class DescriptorImpl extends MatrixConfigurationSorterDescriptor {
        @Override
        public String getDisplayName() {
            return "Last axis order";
        }

    }

}
