Division-by-Zero in charge per child calculation

Arrange:
Add records with 0 children and non-zero charges
Act:
Call method that computes charges per child
Assert:
Should be able to handle with no crash or divide-by-zero error.  Return 0 or Skip record


Regression Test with Perfect Linear Relationship
Arrange:
Create records where charges = 2 * BMI
Act:
Run regression and compute Pearson r
Assert:
r should be approximately 1.0


