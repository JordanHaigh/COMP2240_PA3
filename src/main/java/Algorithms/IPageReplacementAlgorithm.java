package Algorithms;

import Model.Page;


/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * IPageReplacementAlgorithm.java is one of the page replacement algorithms.
 * Algorithm Interface used by concrete classes
 */
public interface IPageReplacementAlgorithm
{
    int getReplacementIndex(Page pageToInsert);
}
