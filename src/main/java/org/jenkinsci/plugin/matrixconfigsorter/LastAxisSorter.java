/*
 * The MIT License
 *
 * Copyright 2011 lucinka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugin.matrixconfigsorter;

import hudson.Extension;
import hudson.matrix.Axis;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixConfigurationSorter;
import hudson.matrix.MatrixConfigurationSorterDescriptor;
import hudson.matrix.MatrixProject;
import hudson.matrix.listeners.MatrixBuildListener;
import hudson.util.FormValidation;
import java.util.Comparator;
import java.util.List;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * 
 * @author lucinka
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
