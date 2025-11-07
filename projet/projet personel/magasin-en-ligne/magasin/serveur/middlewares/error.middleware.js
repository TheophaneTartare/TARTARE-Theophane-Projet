module.exports = (err, req, res, next) => {
  const status = err.status || 404;
  
  console.error(err);

  if (req.xhr || req.headers.accept?.includes('json')) {
    return res.status(status).json({
      error: {
        message: err.message || 'Une erreur interne est survenue.',
        status,
      },
    });
  }

  res.status(status).render('error', { error: err, status });
};
